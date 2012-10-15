package web.hashing;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Collection;

public class BloomFilter<E> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BitSet bitSet;
	private int bitSetSize;
	private double bitsPerElement;
	private int expectedNumOfElements;
	private int numberOfAddedElements;
	private int k;
	
	static final Charset CHARSET=Charset.forName("UTF-8");
	
	static final String HASH_NAME="MD5";
	static  MessageDigest messageDigest=null;
	static{
		MessageDigest md;
		try{
			md=MessageDigest.getInstance(HASH_NAME);
		}catch(NoSuchAlgorithmException ex){
			ex.printStackTrace();
			md=null;
		}
		messageDigest = md;
	}
	
	public BloomFilter(double c,int n,int k){
		this.expectedNumOfElements=n;
		this.k=k;
		this.bitsPerElement=c;
		this.bitSetSize=(int)Math.ceil(c*n);
		numberOfAddedElements=0;
		this.bitSet=new BitSet(bitSetSize);
	}
	
	public BloomFilter(int bitSetSize,int expectedNumberOfElements){
		this(bitSetSize/(double)expectedNumberOfElements,expectedNumberOfElements,(int)Math.round((bitSetSize/(double)expectedNumberOfElements)*Math.log(2.0)));
	}
	public BloomFilter(double falsePositiveProbability,int expectedNumOfElements){
		this(Math.ceil(-(Math.log(falsePositiveProbability)/Math.log(2)))/Math.log(2),expectedNumOfElements,(int)Math.ceil(-(Math.log(falsePositiveProbability)/Math.log(2))));
	}
	public static int createHash(String val,Charset charset){
		return createHash(val.getBytes(charset));
	}
	public static int createHash(String val){
		return createHash(val,CHARSET);
	}
	public static int createHash(byte[] data){
		return createHashes(data,1)[0];
	}
	public static int[] createHashes(byte[] data,int hashes){
		int[] result=new int[hashes];
		
		int k=0;
		byte salt=0;
		while(k<hashes){
			byte[] digest;
			synchronized(messageDigest){
				messageDigest.update(salt);
				salt++;
				digest=messageDigest.digest(data);
			}
			for(int i=0;i<digest.length/4&&k<hashes;i++){
				int h=0;
				for(int j=(i*4);j<(i*4)+4;j++){
					h<<=8;
					h |=((int)digest[j]) & 0xFF;
				}
				result[k]=h;
				k++;
			}
		}
		return result;
	}
	public boolean equals(Object obj){
		if(obj==null){
			return false;
		}
		
		if(getClass()!=obj.getClass()){
			return false;
		}
		BloomFilter<E> o2 = (BloomFilter<E>) obj;
		final BloomFilter<E> other=o2;
		if(this.expectedNumOfElements!=other.expectedNumOfElements){
			return false;
		}
		if(this.k!=other.k){
			return false;
		}
		if(this.bitSetSize!=other.bitSetSize){
			return false;
		}
		if(this.bitSet!=other.bitSet&&(this.bitSet==null)||!this.bitSet.equals(other.bitSet)){
			return false;
		}
		return true;
	}
	public int haseCode(){
		int hash=7;
		hash=61*hash+(this.bitSet!=null?this.bitSet.hashCode() : 0);
		hash=61*hash+this.expectedNumOfElements;
		hash=61*hash+this.bitSetSize;
		hash=61*hash+this.k;
		return hash;
	}
	
	public double expectedFalsePositiveProbability(){
		return getFalsePositiveProbability(this.expectedNumOfElements);
	}
	public double getFalsePositiveProbability(double numOfElements){
		return Math.pow((1-Math.exp(-k*(double)numOfElements/(double)bitSetSize)), k);
	}
	public double getFalsePositiveProbability(){
		return getFalsePositiveProbability(numberOfAddedElements);
	}
	public int getK(){
		return k;
	}
	public void clear(){
		bitSet.clear();
		numberOfAddedElements=0;
	}
	
	public void add(E  element){
		add(element.toString().getBytes(CHARSET));
	}
	public void add(byte[] bytes){
		int[] hashes=createHashes(bytes,k);
		for(int hash:hashes)
			bitSet.set(Math.abs(hash%bitSetSize),true);
		numberOfAddedElements++;
	}
	
	public void addAll(Collection<? extends E> c){
		for(E element : c){
			add(element);
		}
	}
	public boolean contains(E element){
		return contains(element.toString().getBytes(CHARSET));
	}
	
	public boolean contains(byte[] bytes){
		int[] hashes=createHashes(bytes,k);
		for(int hash:hashes){
			if(!bitSet.get(Math.abs(hash%bitSetSize))){
				return false;
			}
		}
		return true;
	}
	
	public boolean containsAll(Collection<? extends E> c){
		for(E element : c)
			if(!contains(element))
				return false;
		return true;
	}
	
	public boolean getBit(int bit){
		return bitSet.get(bit);
	}
	
	public void setBit(int bit,boolean value){
		bitSet.set(bit,value);
	}
	public BitSet getBitSet(){
		return bitSet;
	}
	public int size(){
		return this.bitSetSize;
	}
	public int count(){
		return this.numberOfAddedElements;
	}
	
	public int getExpectedNumberOFElements(){
		return expectedNumOfElements;
	}
	public double getExpectedBitsPerElement(){
		return this.bitsPerElement;
	}
	public double getBitPerElement(){
		return this.bitSetSize/(double)numberOfAddedElements;
	}
}


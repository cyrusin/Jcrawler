package web.hashing;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter implements Serializable {
	private BitSet bitSet;
	private int bitSetSize;
	private double bitsPerElement;
	private int expectedNumberOfFilterElements;
	private int numberOfAddedElements;
	private int k;
	
	static final Charset charset=Charset.forName("UTF-8");
	
	static final String hashName="MD5";
	static  MessageDigest digestFunction=null;
	static{
		MessageDigest tmp;
		try{
			tmp=MessageDigest.getInstance(hashName);
		}catch(NoSuchAlgorithmException ex){
			ex.printStackTrace();
			tmp=null;
		}
		digestFunction = tmp;
	}
	
	public BloomFilter(double c,int n,int k){
		this.expectedNumberOfFilterElements=n;
		this.k=k;
		this.bitsPerElement=c;
		this.bitSetSize=(int)Math.ceil(c*n);
		numberOfAddedElements=0;
		this.bitSet=new BitSet(bitSetSize);
	}
	
	public static int createHash(String val,Charset charset){
		return createHash(val.getBytes(charset));
	}
	public static int createHash(String val){
		return createHash(val,charset);
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
			synchronized(digestFunction){
				digestFunction.update(salt);
				salt++;
				digest=digestFunction.digest(data);
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
}

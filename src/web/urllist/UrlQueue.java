package web.urllist;
import java.util.*;
public class UrlQueue {
	
	private LinkedList queue=new LinkedList();
	
	public void enQueue(Object t){
		queue.addLast(t);
	}
	
	public Object deQueue(){
		return queue.removeFirst();
	}
	
	
	public boolean isUrlQueueEmpty(){
		return queue.isEmpty();
	}
	
	public boolean contains(Object t){
		return queue.contains(t);
	}

}

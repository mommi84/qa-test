package org.aksw.tsoru.qatest2.model;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class NLPNode implements Comparable<NLPNode> {

	private NLPNode parent;
	private String name;
	private int incr = 1;
	private String value;

	public NLPNode(NLPNode parent, String name) {
		this.setParent(parent);
		this.setName(name);
	}

	public NLPNode getParent() {
		return parent;
	}

	public void setParent(NLPNode parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUniqueName() {
		return name + incr;
	}

	public void incr() {
		this.incr++;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		if(parent == null)
			return this.getUniqueName()+"<-null";
		return this.getUniqueName()+"("+value+")<-"+parent.getUniqueName();
	}

	@Override
	public int compareTo(NLPNode o) {
		return this.getUniqueName().compareTo(o.getUniqueName());
	}

}

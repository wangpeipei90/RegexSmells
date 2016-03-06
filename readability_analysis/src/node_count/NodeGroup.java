package node_count;

import java.util.HashSet;

public class NodeGroup extends HashSet<RTNode>{
	private static final long serialVersionUID = -1914991659146708527L;
	private final String groupName;

	public NodeGroup(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupName() {
		return groupName;
	}
}

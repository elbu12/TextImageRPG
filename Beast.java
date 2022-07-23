package main;

public class Beast {
	private RoomScenario beastRoom;
	private byte status;	//bear's intentions
	private byte message;	//special message
	private RoomScenario restartingRoom; //where you restart when you die
	public Beast() {
		beastRoom = null;
		restartingRoom = null;
		status = 0;
		//0 - beast absent
		//1 - beast present, stalking you
		//2 - in courtyard again, final state
		message = 0;
		//0 - no message
		//1 - beast just began hunting you
		//2 - trapped
		//3 - fight without sword
		//4 - fight with sword
		//5 - restarting
		//6 - warning about approaching beast
	}
	public void setBeastRoom(RoomScenario room) {
		this.beastRoom = room;
	}
	public RoomScenario getBeastRoom() {
		return beastRoom;
	}
	public void setRestartingRoom(RoomScenario room) {
		this.restartingRoom = room;
	}
	public RoomScenario getRestartingRoom() {
		return restartingRoom;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public void setStatus(int status) {
		this.status = (byte)(status);
	}
	public byte getStatus() {
		return status;
	}
	public void setMessage(byte message) {
		this.message = message;
	}
	public void setMessage(int message) {
		setMessage((byte)(message));
	}
	public byte getMessage() {
		return message;
	}
}

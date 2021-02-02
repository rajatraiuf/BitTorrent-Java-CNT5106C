/**
 * The data structure which is used by different threads in a host to communicate with each other.
 */

package cnt5106C;

public class InterThreadMessage {
	public String msg;//The context of the msg
	public int index;//Which peer should receive this msg
	public boolean test;//A test message will be sent to remote host directly without any additional logic
	
	/**
	 * The constructor of inter thread message.
	 * @param msg
	 * @param index
	 * @param test
	 */
	public InterThreadMessage(String msg, int index, boolean test) {
		this.msg = msg;
		this.index = index;
		this.test = test;
	}
	
	/**
	 * Upstream handler will execute a inter thread message as soon as it retrieve it.
	 * @param out the Handler to send any msg
	 */
	public void execute(MessageSendingThread out) {
		if(test) {
			out.send(msg);//If this message is supposed to be sent outside, then we do it.
		}else {
			//TODO A very biiiiig todo here, all the protocol logic should be there, with some function calling maybe
		}
	}
}

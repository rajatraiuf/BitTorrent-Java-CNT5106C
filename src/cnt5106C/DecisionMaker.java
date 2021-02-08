/**
 * The real Control Thread, running in parallel, used to decide who to choke, who to unchoke and stuffs like that.
 */

package cnt5106C;

import java.time.LocalTime;

public class DecisionMaker extends Thread{
	/**
	 *Main method of decision maker as a thread.
	 */
	public void run() {
		while(true) {
			/*
			 * The code bellow is only for testing.
			 * The decisionMaker is trying to send a "Hi" message to each connected peer every 5 seconds.
			 */
			try {
				for(DynamicPeerInfo p : ControlSystem.peers) {
					if(p.isConnected) {
						/*
						ControlSystem.messageQueues.get(p.index).put(
								new Message(
										"Hi, i'm peer " + ControlSystem.index + ", it's " + LocalTime.now()
										, p.index
										, false)
								);
						*/
					}
				}
				sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

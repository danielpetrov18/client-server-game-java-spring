package client.player;

/**
 * <p>Holds the data one uses during registration.
 *    The reason why this class is separate is because the data here is not used very frequently.</p> 
 */
public class PlayerRegisterInfo {

	private String uaccount = "";
	private String firstname = "";
	private String lastname = "";
	
	public PlayerRegisterInfo() {}
	
	public PlayerRegisterInfo(final String uacc, final String fname, final String lname) {
		this.uaccount = uacc;
		this.firstname = fname;
		this.lastname = lname;
	}

	public String getUAccount() {
		return this.uaccount;
	}
	
	public String getFirstname() {
		return this.firstname;
	}
	
	public String getLastname() {
		return this.lastname;
	}
	
	public boolean isRegistered() {
		return !this.uaccount.isEmpty();
	}
	
	@Override
	public String toString() {
		return "[uacc=" + this.uaccount + ", fn=" + this.firstname + ", ln=" + this.lastname + "]";
	}
	
}
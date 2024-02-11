package server.main.player;

/*
 * Contains all the registration data that pertains to a particular player.
 */
public class PlayerInfo {
	
	private String firstname = "";
	private String lastname = "";
	private String uaccount = "";
	
	public PlayerInfo (final String fname, final String lname, final String uacc) {
		this.firstname = fname;
		this.lastname = lname;
		this.uaccount = uacc;	
	}
	
	public String getFirstname() {
		return this.firstname;
	}
	
	public String getLastname() {
		return this.lastname;
	}
	
	public String getUAccount() {
		return this.uaccount;
	}
	
	public boolean isRegistered() {
		return !this.uaccount.isEmpty();
	}
	
	@Override
	public String toString() {
		return "fn=" + firstname + ", ln=" + lastname + ", uacc=" + uaccount;
	}
	
}
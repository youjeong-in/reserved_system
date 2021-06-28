package beans;
/* 개발 규칙
 *  - 생성자를 별도로 생성하지 않습니다. --> X
 *  - 저장소인 변수는 필드화 한다.
 *  - 필드는 반드시 외부 접근이 불가능 하도록 private으로 선언한다.
 *  - 하나의 필드에 접근할 수 있는 setter method와 getter method를 생성할 수 있다.
 * */
public class Member {
	private String accessType;
	private String memberId;   // cu_id, re_code
	private String memberPassword;
	private String memberName; // CU_NAME, RE_NAME
	private String memberEtc;  // userPhone, CEOName
	private String categoryCode;
	private String category;
	private String location;
	
	
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberPassword() {
		return memberPassword;
	}
	public void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberEtc() {
		return memberEtc;
	}
	public void setMemberEtc(String memberEtc) {
		this.memberEtc = memberEtc;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
}

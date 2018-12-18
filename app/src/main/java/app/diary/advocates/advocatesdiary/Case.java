package app.diary.advocates.advocatesdiary;

/**
 * Created by khans on 3/26/2018.
 */

public class Case {
    private String case_id,case_number,case_type,complainant_name,complainant_phone,
            complainant_address,opponent_name,opponent_phone,opponent_address,previous_date,next_date,
            court_name,court_type,court_genre,refered_by,comment,user_id;

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getCase_number() {
        return case_number;
    }

    public void setCase_number(String case_number) {
        this.case_number = case_number;
    }

    public String getCase_type() {
        return case_type;
    }

    public void setCase_type(String case_type) {
        this.case_type = case_type;
    }

    public String getComplainant_name() {
        return complainant_name;
    }

    public void setComplainant_name(String complainant_name) {
        this.complainant_name = complainant_name;
    }

    public String getComplainant_phone() {
        return complainant_phone;
    }

    public void setComplainant_phone(String complainant_phone) {
        this.complainant_phone = complainant_phone;
    }

    public String getComplainant_address() {
        return complainant_address;
    }

    public void setComplainant_address(String complainant_address) {
        this.complainant_address = complainant_address;
    }

    public String getOpponent_name() {
        return opponent_name;
    }

    public void setOpponent_name(String opponent_name) {
        this.opponent_name = opponent_name;
    }

    public String getOpponent_phone() {
        return opponent_phone;
    }

    public void setOpponent_phone(String opponent_phone) {
        this.opponent_phone = opponent_phone;
    }

    public String getOpponent_address() {
        return opponent_address;
    }

    public void setOpponent_address(String opponent_address) {
        this.opponent_address = opponent_address;
    }

    public String getPrevious_date() {
        return previous_date;
    }

    public void setPrevious_date(String previous_date) {
        this.previous_date = previous_date;
    }

    public String getNext_date() {
        return next_date;
    }

    public void setNext_date(String next_date) {
        this.next_date = next_date;
    }

    public String getCourt_name() {
        return court_name;
    }

    public void setCourt_name(String court_name) {
        this.court_name = court_name;
    }

    public String getCourt_type() {
        return court_type;
    }

    public void setCourt_type(String court_type) {
        this.court_type = court_type;
    }

    public String getCourt_genre() {
        return court_genre;
    }

    public void setCourt_genre(String court_genre) {
        this.court_genre = court_genre;
    }

    public String getRefered_by() {
        return refered_by;
    }

    public void setRefered_by(String refered_by) {
        this.refered_by = refered_by;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public static String[] getCaseno() {
        return caseno;
    }

    public static void setCaseno(String[] caseno) {
        Case.caseno = caseno;
    }

    public static String[] getCasename() {
        return casename;
    }

    public static void setCasename(String[] casename) {
        Case.casename = casename;
    }

    public static String[] getCaseaddress() {
        return caseaddress;
    }

    public static void setCaseaddress(String[] caseaddress) {
        Case.caseaddress = caseaddress;
    }

    public static String[] caseno={
            "001","002","003","004","005","006","007","008","009","010","011","012"
    };
    public static String[] casename={
            "case001","case002","case003","case004","case005","case006","case007","case008","case009","case010","case011","case012"
    };
    public static String[] caseaddress={
            "address001","address002","address003","address004","address005","address006","address007","address008","address009","address010","address011","address012"
};
}

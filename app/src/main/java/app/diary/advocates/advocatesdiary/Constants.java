package app.diary.advocates.advocatesdiary;

/**
 * Created by khans on 3/28/2018.
 */

public class Constants {
    public static final String ROOT_URL="http://test.proadvocatediary.com/digitaladvocates/public/index.php/api/";
    public static final String ROOT_CASE_URL="http://test.proadvocatediary.com/digitaladvocates/public/caseindex.php/api/";

    public static final String URL_REGISTER=ROOT_URL+"advocates_registrations";
    public static final String URL_LOGIN=ROOT_URL+"user_login";
    public static final String URL_FORGET_PASSWORD=ROOT_URL+"forget_password";
    public static final String URL_PASSWORD_CHANGE=ROOT_URL+"user_passwordchange";
    public static final String URL_SUB_PASSWORD_CHANGE=ROOT_URL+"sub_user_passwordchange";
    public static final String URL_ALL_SUB_USERS=ROOT_URL+"find_subUsers";
    public static final String URL_DELETE_SUB_USERS=ROOT_URL+"delete_sub_users";
    public static final String URL_RECOVER_CHANGE=ROOT_URL+"recover_password";

    public static final String URL_ADD_CASE=ROOT_CASE_URL+"case_registrations";
    public static final String URL_GET_CASES=ROOT_CASE_URL+"all_cases_show";
    public static final String URL_UPDATE_CASES=ROOT_CASE_URL+"case_update";
    public static final String URL_DELETE_CASES=ROOT_CASE_URL+"case_delete";
}

import DAO.MyDAO;
import datasets.UserDataSet;
import dbcommon.ConnectionHelper;
import java.sql.SQLException;

public class Test {

    public static void main(String[] args) throws SQLException {
        MyDAO myDAO = new MyDAO(ConnectionHelper.getMyPostgresqlConnection());

        UserDataSet vasjanPro = new UserDataSet("vasjanPro", 13);
        UserDataSet nagibator2005 = new UserDataSet("nagibator2005", 12);
        UserDataSet xXxmegaKillerxXx = new UserDataSet("xXxmegaKillerxXx", 11);

        myDAO.save(vasjanPro);
        myDAO.save(nagibator2005);
        myDAO.save(xXxmegaKillerxXx);

        System.out.println("VasjanPro id = " + vasjanPro.getId());
        System.out.println("nagibator2005 id = " + nagibator2005.getId());
        System.out.println("xXxmegaKillerxXx id = " + xXxmegaKillerxXx.getId());


        UserDataSet vasjanProGamer = myDAO.load(vasjanPro.getId(),  UserDataSet.class);
        UserDataSet nagibator2005Gamer = myDAO.load(nagibator2005.getId(),  UserDataSet.class);
        UserDataSet xXxmegaKillerxXxGamer = myDAO.load(xXxmegaKillerxXx.getId(),  UserDataSet.class);

        System.out.println(vasjanProGamer);
        System.out.println(nagibator2005Gamer);
        System.out.println(xXxmegaKillerxXxGamer);
    }
}

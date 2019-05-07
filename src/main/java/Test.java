import DAO.MyDAO;
import datasets.DataSet;
import datasets.UserDataSet;
import dbcommon.ConnectionHelper;
import executors.Executor;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) throws SQLException {
        MyDAO myDAO = new MyDAO(ConnectionHelper.getMyPostgresqlConnection());

        UserDataSet vasjanPro = new UserDataSet("vasjanPro", 13);

        myDAO.save(vasjanPro);

        System.out.println("VasjanPro id = " + vasjanPro.getId());


        UserDataSet vasjanProGamer = myDAO.load(vasjanPro.getId(),  UserDataSet.class);

        System.out.println(vasjanProGamer);

    }
}

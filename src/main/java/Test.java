
import datasets.AddressDataSet;
import datasets.PhoneDataSet;
import datasets.UserDataSet;
import services.DBServiceHibernateImpl;
import java.sql.SQLException;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws SQLException {

        DBServiceHibernateImpl service = new DBServiceHibernateImpl();

        UserDataSet vasjanPro = new UserDataSet("vasjanPro",
                13,
                Arrays.asList(new PhoneDataSet("880005553535")),
                new AddressDataSet("вычисляется по IP"));

        UserDataSet nagibator2005 = new UserDataSet("nagibator2005",
                12,
                Arrays.asList(new PhoneDataSet("nagibator2005_1"), new PhoneDataSet("nagibator2005_2")),
                null);
        UserDataSet xXxmegaKillerxXx = new UserDataSet("xXxmegaKillerxXx",
                11,
                null,
                new AddressDataSet("я твой труба шатал"));

        service.save(vasjanPro);
        service.save(nagibator2005);
        service.save(xXxmegaKillerxXx);

        System.out.println("VasjanPro id = " + vasjanPro.getId());
        System.out.println("nagibator2005 id = " + nagibator2005.getId());
        System.out.println("xXxmegaKillerxXx id = " + xXxmegaKillerxXx.getId());


        UserDataSet vasjanProGamer = service.load(vasjanPro.getId(),  UserDataSet.class);
        UserDataSet nagibator2005Gamer = service.load(nagibator2005.getId(),  UserDataSet.class);
        UserDataSet xXxmegaKillerxXxGamer = service.load(xXxmegaKillerxXx.getId(),  UserDataSet.class);

        System.out.println(vasjanProGamer);
        System.out.println(nagibator2005Gamer);
        System.out.println(xXxmegaKillerxXxGamer);
    }
}

package lucasdasilvac.dev.androidvirtualstore.Common;

import lucasdasilvac.dev.androidvirtualstore.Model.User;

public class Common {
    public static User currentUser;

    public static String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "realizado";
        else if (status.equals("1"))
            return "a caminho";
        else
            return "enviado";
    }
}

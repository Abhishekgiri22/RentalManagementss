package com.dee.rentalmanagement;

import com.dee.rentalmanagement.bbl.LoginBBL;

import org.junit.Assert;
import org.junit.Test;

public class UnitTesting {
    String success;

    @Test
    public void testLogin() {
        LoginBBL loginBBL = new LoginBBL();
        Boolean result = loginBBL.checkUser("", "#Helloworld545");
        Assert.assertEquals(true, result);
    }

    @Test
    public void logintest() {
        LoginBBL loginBBL = new LoginBBL();
        Boolean result = loginBBL.checkUser("", "#Helloworld545");
        Assert.assertEquals(false, result);
    }


}

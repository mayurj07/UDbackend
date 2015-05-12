package com.udelvr.driver;

import com.udelvr.login.driver.DriverController;
import com.udelvr.login.driver.DriverDO;
import com.udelvr.login.driver.DriverService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(value = MockitoJUnitRunner.class)
public class DriverServiceTest {
    @Test
    public void getDriverDetails() throws Exception {

        DriverService driverService = mock(DriverService.class);
        final DriverDO driverDO = new DriverDO("5", "Neeraja Kukday", "nk@gmail.com", "123", "testpass", "NexusID","License123","02-02-2015",4,"test.com","profile.com");
        when(driverService.getDriver("5")).thenReturn(driverDO);
        DriverController getDriverResult = new DriverController(driverService);
        //when
        DriverDO tempDriver = getDriverResult.getDriver("5");
        //then
        //assertThat(tempDriver).isNotNull();
        //pass test
        assert tempDriver.getEmail().equals("nk@gmail.com");
        assert tempDriver.getDriverRating().equals(4);
        assertThat("Error in password", tempDriver.getPassword().equals("testpass"));

        //to fail test
//        assert tempDriver.getEmail().equals("nk1@gmail.com");
//        assert tempDriver.getDriverRating().equals(3);
//        assertThat("Error in password", tempDriver.getPassword().equals("testpass123"));
    }

}

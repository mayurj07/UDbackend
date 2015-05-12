package com.udelvr.user;

import com.udelvr.login.User.UserDO;
import com.udelvr.login.signUp.SignupController;
import com.udelvr.login.signUp.SignupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(value = MockitoJUnitRunner.class)
public class UserTest {
    @Test
    public void getUserData() throws Exception {

        SignupService signupService = mock(SignupService.class);
        final UserDO user = new UserDO("5", "Neeraja Kukday", "nk@gmail.com", "123", "testpass", "NexusID","profile.com");
        when(signupService.getUserData("5")).thenReturn(user);
        SignupController signupController = new SignupController(signupService);
        //when
        ResponseEntity<UserDO> tempUser = signupController.get_user("5");
        //then
        //assertThat(tempDriver).isNotNull();
        //pass test
        assert tempUser.getBody().getEmail().equals("nk@gmail.com");
        assert tempUser.getBody().getMobileNo().equals("123");

        //fail test
        //assert tempUser.getBody().getEmail().equals("nk1@gmail.com");
        //assert tempUser.getBody().getMobileNo().equals("1234");

    }

}

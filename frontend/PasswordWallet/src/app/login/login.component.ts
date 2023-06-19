import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../services/authentication-service.service';

import { LoginForm } from '../loginForm'
import { UserInfo } from '../userInfo'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: LoginForm = new LoginForm();
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';

  submitted = false;

  constructor(private authenticationService: AuthenticationService) {  }

  ngOnInit(): void {
    //this.authenticationService.signOut()
    if (this.authenticationService.userInfoIsPresent()) {
      this.isLoggedIn = true;
      this.redirectToHomePage();
    }
  }

  onSubmit(): void {
    this.submitted = true;

    this.authenticationService.login(this.loginForm).subscribe(
      data => {
        this.authenticationService.saveUserInfoAfterLoggingIn
        (new UserInfo(data.userId, data.masterLogin, data.lastSuccessfulLoginDate, data.lastUnsuccessfulLoginDate));

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.redirectToHomePage();
      }, error => {
        this.errorMessage = error.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  redirectToHomePage(): void {
    window.location.replace('/home');
  }

  redirectToRegisterPage(): void {
    window.location.replace('/register');
  }
}

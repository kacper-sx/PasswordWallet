import { Component, OnInit } from '@angular/core';

import { AuthenticationService } from './services/authentication-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  title = 'PasswordWallet';

  public userIsLoggedIn: boolean = false;
  public masterLogin: string = '';

constructor(private authenticationService: AuthenticationService) {
  this.userIsLoggedIn = this.authenticationService.userInfoIsPresent();
  this.masterLogin = this.authenticationService.getUserInfo().masterLogin;
 }

  ngOnInit(): void {

  }


  logout() {
    this.userIsLoggedIn = false;
    this.authenticationService.signOut();
  }
}

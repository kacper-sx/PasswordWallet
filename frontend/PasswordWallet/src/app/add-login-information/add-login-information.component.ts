import { Component, OnInit } from '@angular/core';

import { AuthenticationService } from '../services/authentication-service.service';
import { LoginInfoService } from '../services/login-info-service.service';

import { LoginInfoAdd } from '../loginInfoAdd'

@Component({
  selector: 'app-add-login-information',
  templateUrl: './add-login-information.component.html',
  styleUrls: ['./add-login-information.component.css']
})
export class AddLoginInformationComponent implements OnInit {

  errorMessage: string = '';
  isAddingFailed: boolean = false;
  loginInfoAddForm: LoginInfoAdd = new LoginInfoAdd();
  reEnteredPassword: string = '';

  constructor(private loginInfoService: LoginInfoService, private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
  }

  onSubmit() {
    this.loginInfoService.addLoginInfo(this.authenticationService.getUserInfo().userId, this.loginInfoAddForm).subscribe (
      data => {
        this.isAddingFailed = false;
        this.redirectToHome();
      }, error => {
        this.errorMessage = error.error.message;
        this.isAddingFailed = true;
      }
    );
  }

  redirectToHome(): void {
    window.location.replace('/home');
  }
}

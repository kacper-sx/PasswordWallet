import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import { AuthenticationService } from '../services/authentication-service.service';

import { PasswordChangeForm } from '../passwordChangeForm';

import { UserInfo } from '../userInfo'

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  @ViewChild('successModal') successModal?: TemplateRef<any>;

  passwordChangeForm: PasswordChangeForm = new PasswordChangeForm(-1, '', '');
  public userIsLoggedIn: boolean = false;
  public showPasswordChangeErrorMessage: boolean = false;
  public message: string = '';
  public newPasswordRepeat: string = '';

  userInfo: UserInfo;
  public lastSuccessfulLoginDate: string = '';
  public lastUnsuccessfulLoginDate: string  = '';

  constructor(private authenticationService: AuthenticationService, private modalService: NgbModal) {
    this.userIsLoggedIn = this.authenticationService.userInfoIsPresent();

    this.userInfo = this.authenticationService.getUserInfo();
    this.lastSuccessfulLoginDate = this.userInfo.lastSuccessfulLoginDate;
    this.lastUnsuccessfulLoginDate = this.userInfo.lastUnsuccessfulLoginDate;
  }

  ngOnInit(): void {
    if(!this.userIsLoggedIn) {
      this.redirectToLoginPage();
    }
  }

  passwordSubmit() {
    this.passwordChangeForm.userId = this.authenticationService.getUserInfo().userId;
    this.authenticationService.changePassword(this.passwordChangeForm).subscribe(
      data => {
        console.log(data);
        this.message = data.message;
        this.showPasswordChangeErrorMessage = false;
        this.openSuccessModal();
      }, error => {
        console.log(error);
        this.message = error.error.message;
        this.showPasswordChangeErrorMessage = true;
      }
    );
  }

  openSuccessModal() {
    this.modalService.open(this.successModal, { centered: true });
  }

  openPasswordChangeModal(content: any) {
    this.modalService.open(content, { centered: true });
  }

  redirectToLoginPage() {
    window.location.replace('/login');
  }

  refreshPage() {
    window.location.replace('/profile');
  }
}

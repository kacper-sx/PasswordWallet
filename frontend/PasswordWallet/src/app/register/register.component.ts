import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {AuthenticationService} from '../services/authentication-service.service';
import {LoginComponent} from '../login/login.component';
import {Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import { RegisterForm } from '../registerForm'

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  @ViewChild('registerSuccessModal') registerSuccessModal?: TemplateRef<any>;

  registerForm: RegisterForm = new RegisterForm();
  isRegistrationFailed: boolean = false;
  message = '';

  constructor(private authenticationService: AuthenticationService, private loginComponent: LoginComponent,
   private router: Router, private modalService: NgbModal) { }

  ngOnInit(): void {
    if (this.authenticationService.userInfoIsPresent()) {
      this.redirectToHomePage();
    }
  }

  registerSubmit(): void {
    this.authenticationService.register(this.registerForm).subscribe(
      data => {
        this.isRegistrationFailed = false;
        this.message = 'Registration successful!';
        this.openSuccessModal();
      }, error => {
        this.isRegistrationFailed = true;
        console.log(error);
        this.message = error.error.message;
      }
    );
  }

  openSuccessModal() {
    this.modalService.open(this.registerSuccessModal, { centered: true });
  }

  redirectToHomePage() {
    window.location.replace('/home');
  }

  automaticLoginAfterRegistration() {
    this.loginComponent.loginForm.masterLogin = this.registerForm.masterLogin;
    this.loginComponent.loginForm.masterPassword = this.registerForm.masterPassword;
    this.loginComponent.onSubmit();
  }
}

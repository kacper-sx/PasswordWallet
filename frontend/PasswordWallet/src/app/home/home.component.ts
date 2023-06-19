import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { AuthenticationService } from '../services/authentication-service.service';
import { LoginInfoService } from '../services/login-info-service.service';

import { LoginInfoEditionForm } from '../loginInfoEditionForm'

import { LoginInfoRead } from '../loginInfoRead'
import { PaginationConfig } from '../paginationConfig'
import { DecodePasswordRequest } from '../decodePasswordRequest'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public loginInformationsList: LoginInfoRead[] = [];
  public paginationConfig: PaginationConfig = new PaginationConfig(1, 5, 0);

  public idOfLoginInfoToDelete: number = -1;
  public idOfLoginInfoToDecode: number = -1;

  public loginInfoEditionForm: LoginInfoEditionForm = new LoginInfoEditionForm('');
  public idOfLoginInfoToEdit: number = 0;

  public userIsLoggedIn: boolean = false;

  public decodePasswordForm: DecodePasswordRequest = new DecodePasswordRequest('');
  public aPasswordWasDecodedOnce: boolean = false;
  public isDecodingFailed: boolean  = false;
  public isEditInfoFailed: boolean  = false;
  public errorMessage: string = '';

  constructor(private authenticationService: AuthenticationService, private loginInfoService: LoginInfoService,
    private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {
    this.userIsLoggedIn = this.authenticationService.userInfoIsPresent();

    activatedRoute.queryParams.subscribe(
    params => this.paginationConfig.currentPage = params.page ? params.page : 1);
  }


  ngOnInit(): void {
    if(!this.userIsLoggedIn) {
      this.redirectToLoginPage();
    } else {
      this.fetchData();
    }
  }

  private fetchData() {
    this.loginInfoService.getAllLoginInformations(this.authenticationService.getUserInfo().userId).subscribe(
    data => {
        this.loginInformationsList = data as LoginInfoRead[];
    });
  }

  openDeleteLoginInfoConfirmationModal(content: any, idOfLoginInfoToDelete: number) {
    this.idOfLoginInfoToDelete = idOfLoginInfoToDelete;
    this.modalService.open(content, { centered: true });
  }

  openDecodePasswordModal(content: any, idOfLoginInfoToDecode: number) {
    if(!this.aPasswordWasDecodedOnce) {
      this.idOfLoginInfoToDecode = idOfLoginInfoToDecode;
      this.modalService.open(content, { centered: true });
    } else {
      this.loginInfoService.decodePassword(
      this.authenticationService.getUserInfo().userId, idOfLoginInfoToDecode, this.decodePasswordForm).subscribe(
        data => {
          this.aPasswordWasDecodedOnce = true;
          this.loginInformationsList[idOfLoginInfoToDecode-1].password = data.password;
        }, error => {
          console.log(error);
          this.errorMessage = error.error.message;
          this.isDecodingFailed = true;
        }
      );
    }
  }

  deleteLoginInfo(idOfLoginInfoToDelete: number) {
    this.loginInfoService.deleteLoginInfo(this.authenticationService.getUserInfo().userId, idOfLoginInfoToDelete).subscribe(
      value => {
        this.refreshPage();
    });
  }

  decodePassword(): void {
    this.loginInfoService.decodePassword(
    this.authenticationService.getUserInfo().userId, this.idOfLoginInfoToDecode, this.decodePasswordForm).subscribe(
      data => {
        this.aPasswordWasDecodedOnce = true;
        this.loginInformationsList.find(item => item.id === this.idOfLoginInfoToDecode)!.password = data.password;
      }, error => {
        this.errorMessage = error.error.message;
        this.isDecodingFailed = true;
      }
    );
  }

  editLoginInfoPassword(): void{
      this.loginInfoService.editLoginInfoPassword(
        this.authenticationService.getUserInfo().userId, this.idOfLoginInfoToEdit, this.loginInfoEditionForm).subscribe(
        data => {
        this.refreshPage();
        }, error => {
          this.errorMessage = error.error.message;
          this.isEditInfoFailed = true;
        }
      );
    }

    openEditLoginInfoModal(content: any, idOfLoginInfoToEdit: number) {
      this.idOfLoginInfoToEdit = idOfLoginInfoToEdit;
      this.modalService.open(content, { centered: true });
    }

  changePage(newPage: number) {
    this.router.navigate(['/home/'], {queryParams: {page: newPage}});
  }

  refreshPage() {
    window.location.replace(this.router.url);
  }

  redirectToLoginPage() {
    window.location.replace('/login');
  }

  logout(): void {
    this.authenticationService.signOut();
  }
}

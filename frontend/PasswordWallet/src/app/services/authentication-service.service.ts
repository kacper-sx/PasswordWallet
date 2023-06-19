import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import { LoginForm } from '../loginForm'
import { RegisterForm } from '../registerForm'
import { UserInfo } from '../userInfo'
import { PasswordChangeForm } from '../passwordChangeForm';

const API_URL = 'http://localhost:8080/';
const USER_INFO = 'user_info';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) { }

  login(loginForm: LoginForm): Observable<any> {
    return this.http.post(API_URL + 'login', loginForm);
  }

  register(registerForm: RegisterForm): Observable<any> {
    return this.http.post(API_URL + 'register', registerForm);
  }

  changePassword(passwordChangeForm: PasswordChangeForm): Observable<any> {
    return this.http.patch(API_URL + 'change/password', passwordChangeForm);
  }

  public saveUserInfoAfterLoggingIn(userInfo: UserInfo): void {
    window.sessionStorage.removeItem(USER_INFO);
    window.sessionStorage.setItem(USER_INFO, JSON.stringify(userInfo));
  }

  public getUserInfo(): UserInfo {
    var UserInfo = JSON.parse(sessionStorage.getItem(USER_INFO)!) || '';
    return UserInfo;
  }

  public userInfoIsPresent() : boolean {
    var UserInfo = this.getUserInfo();
    return UserInfo.userId !== undefined && UserInfo.masterLogin !== undefined;
  }

  public isLoggedIn(): boolean {
    return !!this.getUserInfo();
  }

  signOut() {
    window.sessionStorage.clear();
  }
}

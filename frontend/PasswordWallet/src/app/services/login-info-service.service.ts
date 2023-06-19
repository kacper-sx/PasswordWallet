import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

import { LoginInfoEditionForm } from '../loginInfoEditionForm'

import { LoginInfoAdd } from '../loginInfoAdd'
import { DecodePasswordRequest } from '../decodePasswordRequest'

const API_URL = 'http://localhost:8080/';

@Injectable({
  providedIn: 'root'
})
export class LoginInfoService {

  constructor(private http: HttpClient) { }

  getAllLoginInformations(userId: number): Observable<any> {
    return this.http.get(API_URL + 'users/' + userId + '/logininfos');
  }

  getDecodedLoginInfoById(userId: number, loginInfoId: number): Observable<any> {
    return this.http.get(API_URL + 'users/' + userId + '/logininfos/' + loginInfoId + '/decode');
  }

  addLoginInfo(userId: number, loginInfoAdd: LoginInfoAdd): Observable<any> {
    return this.http.post(API_URL + 'users/' + userId + '/logininfos', loginInfoAdd);
  }

  deleteLoginInfo(userId: number, loginInfoId: number): Observable<any> {
    return this.http.delete(API_URL + 'users/' + userId + '/logininfos/' + loginInfoId);
  }

  decodePassword(userId: number, loginInfoId: number, decodePasswordRequest: DecodePasswordRequest): Observable<any> {
    return this.http.post(API_URL + 'users/' + userId + '/logininfos/' + loginInfoId + '/decode', decodePasswordRequest);
  }

  editLoginInfoPassword(userId: number, loginInfoId: number, loginInfoEditionForm: LoginInfoEditionForm): Observable<any> {
      return this.http.post(API_URL + 'users/' + userId + '/logininfos/' + loginInfoId + '/edit', loginInfoEditionForm);
    }
}

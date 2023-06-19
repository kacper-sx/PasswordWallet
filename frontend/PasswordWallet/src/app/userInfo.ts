export class UserInfo {
  userId: number = -1;
  masterLogin: string = '';
  lastSuccessfulLoginDate: string = '';
  lastUnsuccessfulLoginDate: string = '';

  constructor (userId: number, masterLogin: string, lastSuccessfulLoginDate: string, lastUnsuccessfulLoginDate: string) {
    this.userId = userId;
    this.masterLogin = masterLogin;
    this.lastSuccessfulLoginDate = lastSuccessfulLoginDate;
    this.lastUnsuccessfulLoginDate = lastUnsuccessfulLoginDate;
  }
}

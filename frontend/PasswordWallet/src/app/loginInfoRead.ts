export class LoginInfoRead {
  id: number;
  login: string;
  password: string;
  webAddress: string;
  description: string;

  constructor(id: number, login: string, password: string, webAddress: string, description: string) {
    this.id = id;
    this.login = login;
    this.password = password;
    this.webAddress = webAddress;
    this.description = description;
  }
}

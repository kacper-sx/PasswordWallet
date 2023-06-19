export class DecodePasswordRequest {

  masterPassword: string;

  constructor(masterPassword: string) {
    this.masterPassword = masterPassword;
  }
}

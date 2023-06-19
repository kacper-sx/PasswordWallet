export class PasswordChangeForm {
  userId: number
  oldMasterPassword: string;
  newMasterPassword: string;

  constructor(userId: number, oldMasterPassword: string, newMasterPassword: string) {
    this.userId = userId;
    this.oldMasterPassword = oldMasterPassword;
    this.newMasterPassword = newMasterPassword;

  }
}

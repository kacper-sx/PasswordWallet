import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AppComponent } from './app.component'
import { LoginComponent } from './login/login.component'
import { HomeComponent } from './home/home.component'
import { RegisterComponent } from './register/register.component'
import { ProfileComponent } from './profile/profile.component'
import { AddLoginInformationComponent } from './add-login-information/add-login-information.component'


export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'logininfos/add', component: AddLoginInformationComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

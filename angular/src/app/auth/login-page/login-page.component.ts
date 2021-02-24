import { Component, OnInit } from '@angular/core';
import {AuthService} from "../shared/services/auth.service";
import {Credentials} from "../shared/model/credentials.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent{
  email: string
  password: string

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  loginButtonHandler() {
    if(this.email && this.password){
      const credentials: Credentials = {
        username: this.email,
        password: this.password
      }

      console.log('Try login', credentials);
      this.authService.login(credentials).subscribe(response => {
        console.log(response);
        console.log('successfull login!');
        this.router.navigate(['/system', 'docs'])
      })
    }
  }
}

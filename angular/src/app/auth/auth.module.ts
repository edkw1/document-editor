import {NgModule} from "@angular/core";
import { AuthLayoutComponent } from './shared/components/auth-layout/auth-layout.component';
import { LoginPageComponent } from './login-page/login-page.component';
import {CommonModule} from "@angular/common";
import {RouterModule} from "@angular/router";
import {FormsModule} from "@angular/forms";


@NgModule({

  declarations: [
    AuthLayoutComponent,
    LoginPageComponent,
  ],
    imports: [
        CommonModule,
        RouterModule.forChild([
            {path: '', pathMatch: 'full', redirectTo: '/auth/login'},
            {
                path: '', component: AuthLayoutComponent, children: [
                    {path: 'login', component: LoginPageComponent},
                ]
            }
        ]),
        FormsModule
    ],
  exports: [
    RouterModule,
  ],
})
export class AuthModule{}

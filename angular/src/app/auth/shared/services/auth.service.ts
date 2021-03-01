import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {Injectable} from "@angular/core";
import {Credentials} from "../model/credentials.model";
import {catchError, tap} from "rxjs/operators";
import {environment} from "../../../../environments/environment";
import {AuthResponse} from "../model/auth-response.model";

@Injectable({
  providedIn: "root"
})
export class AuthService {
  private static TOKEN_FIELD_NAME = 'token'
  private static FULL_NAME_FIELD_NAME = 'name'
  private static AUTHORITIES_FIELD_NAME = 'authorities'


  constructor(
    private http: HttpClient
  ) { }


  get token(): string {
    // const expDate = new Date(localStorage.getItem('fb-token-exp'))
    // if (new Date() > expDate) {
    //   this.logout()
    //   return null
    // }
    return localStorage.getItem(AuthService.TOKEN_FIELD_NAME)
  }

  get username(): string {
    return localStorage.getItem(AuthService.FULL_NAME_FIELD_NAME)
  }

  get serviceUser(): boolean{
    return this.getAuthorities().includes('ROLE_SERVICE')
  }

  getAuthorities(): string[] {
    return JSON.parse(localStorage.getItem(AuthService.AUTHORITIES_FIELD_NAME))
  }

  login(credentials: Credentials): Observable<AuthResponse> {
    return this.http.post(`${environment.host}/api/v1/auth/login`, credentials)
      .pipe(
        tap(this.setToken),
        catchError(this.handleError.bind(this))
      )
  }

  logout(): void {
    console.log('logout from system')
    this.setToken(null)
  }

  isAuthenticated(): Promise<boolean> {
    return new Promise( resolve => {
      resolve(!!this.token)
    })
  }


  private handleError(error: HttpErrorResponse) {
    console.log('Auth error', error);
    this.logout();
    return throwError(error);
  }

  setToken(response: AuthResponse) {
    if (response) {
      //const expDate = new Date(new Date().getTime() + +response.expiresIn * 1000)
      localStorage.setItem(AuthService.TOKEN_FIELD_NAME, response.token)
      localStorage.setItem(AuthService.FULL_NAME_FIELD_NAME, response.username)
      // localStorage.setItem(AuthService.AUTHORITIES_FIELD_NAME, JSON.stringify(
      //   response.authorities.map(auth => {
      //     return auth['authority']
      //   })
      // ))
      //localStorage.setItem('fb-token-exp', expDate.toString())
    } else {
      localStorage.clear()
    }
  }
}

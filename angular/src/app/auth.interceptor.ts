import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {AuthService} from "./auth/shared/services/auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private authService: AuthService
  ) {}

  intercept(req: HttpRequest<any>,
            next: HttpHandler): Observable<HttpEvent<any>> {
    if(this.authService.token){
      const clonedRequest = req.clone({
        headers: req.headers.append('Authorization', `Bearer ${this.authService.token}`)
      });
      return next.handle(clonedRequest);
    }

    return next.handle(req);
  }

}

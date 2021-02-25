import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../../environments/environment";
import {FileInfo} from "../model/file-info.model";
import {AuthService} from "../../../auth/shared/services/auth.service";

@Injectable({
  providedIn: 'root'
})
export class FileService {
  API_PATH = '/api/v1/files'

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  uploadFile(file: File): Observable<any>{
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post(`${environment.host+this.API_PATH}`, formData);
  }

  deleteFile(id){
    return this.http.delete(`${environment.host+this.API_PATH}/${id}`)
  }

  fetchFilesList(): Observable<FileInfo[]> {
    return this.http.get<FileInfo[]>(`${environment.host+this.API_PATH}`);
  }

  getDownloadFileUrl(id): string{
    return `${environment.host+this.API_PATH}/${id}/download?key=${this.authService.token}`;
  }

  getFileInfo(id: number): Observable<FileInfo> {
    return this.http.get<FileInfo>(`${environment.host+this.API_PATH}/${id}`);
  }
}

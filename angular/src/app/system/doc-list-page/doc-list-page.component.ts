import {Component, OnInit} from '@angular/core';
import {FileService} from "../shared/services/file.service";
import {FileInfo} from "../shared/model/file-info.model";
import * as FileSize from "filesize";

@Component({
  selector: 'app-doc-list-page',
  templateUrl: './doc-list-page.component.html',
  styleUrls: ['./doc-list-page.component.scss']
})
export class DocListPageComponent implements OnInit{
  files: FileInfo[] = []
  fileToUpload: File
  FileSize = FileSize

  constructor(
    private fs:FileService
  ) { }


  handleFileInput(event: any) {
    this.fileToUpload = event.target.files[0]
  }

  handleFileUpload(){
    this.fs.uploadFile(this.fileToUpload).subscribe(fileInfo => {
      this.fileToUpload = null
      this.files.push(fileInfo)
    })
  }


  ngOnInit(): void {
    this.fs.fetchFilesList().subscribe(files => {
      this.files = files
    })
    this.fs.test().subscribe(t => {
      console.log(t);
    })
  }

  downloadUrl({id}) {
    return this.fs.getDownloadFileUrl(id)
  }

  deleteFile({id, name}) {
    if(confirm(`Are you sure want to remove file "${name}"?`)){
      this.fs.deleteFile(id).subscribe(_ => {
        this.files = this.files.filter(f => f.id !== id)
      })
    }
  }
}


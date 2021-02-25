import {Component, OnInit} from '@angular/core';
import {FileService} from "../shared/services/file.service";
import {FileInfo} from "../shared/model/file-info.model";

@Component({
  selector: 'app-doc-list-page',
  templateUrl: './doc-list-page.component.html',
  styleUrls: ['./doc-list-page.component.scss']
})
export class DocListPageComponent implements OnInit{
  files: FileInfo[] = []
  fileToUpload: File

  constructor(
    private fs:FileService
  ) { }


  handleFileInput(event: any) {
    this.fileToUpload = event.target.files[0]
  }

  handleFileUpload(){
    this.fs.uploadFile(this.fileToUpload).subscribe(data => {
      this.fileToUpload = null
      console.log('successfull loaded');
    })
  }


  ngOnInit(): void {
    this.fs.fetchFilesList().subscribe(files => {
      this.files = files
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


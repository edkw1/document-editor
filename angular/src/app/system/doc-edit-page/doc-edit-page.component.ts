import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {environment} from "../../../environments/environment";
import {AuthService} from "../../auth/shared/services/auth.service";
import {ActivatedRoute} from "@angular/router";
import {FileService} from "../shared/services/file.service";
import {FileInfo} from "../shared/model/file-info.model";


declare let DocsAPI: any;

@Component({
  selector: 'app-doc-edit-page',
  templateUrl: './doc-edit-page.component.html',
  styleUrls: ['./doc-edit-page.component.scss']
})
export class DocEditPageComponent implements OnInit, AfterViewInit, OnDestroy {
  docEditor: any

  constructor(
    private fs: FileService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    const view = this.route.snapshot.data['view']
    const id = this.route.snapshot.params['id'];

    this.fs.getFileInfo(id).subscribe(fileInfo => {

      const config = {
        document: {
          fileType: 'docx',
          title: fileInfo.name,
          url: this.fs.getDownloadFileUrl(fileInfo.id)
        },
        documentType: 'word',
        width: '100%',
        height: '97%',
        editorConfig: {
          mode: view ? 'view' : 'edit',
          lang: 'ru',
          //callbackUrl: 'http://192.168.56.1/test',
          user: {
            id: '1',
            name: 'user'
          },
          customization: {
            autosave: true,
            forcesave: true,
            compactHeader: false,
            macros: false,
            plugins: false,
            unit: 'cm',
            comments: false,
            chat: false,
            feedback: false
          }
        }
      };

      this.docEditor = new DocsAPI.DocEditor('doc', config);
    })

  }

  ngOnDestroy() {
    if (this.docEditor) {
      this.docEditor.destroyEditor();
    }
  }

}

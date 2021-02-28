import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {FileService} from "../shared/service/file.service";


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
    const view = this.route.snapshot.data['view'];
    const id = this.route.snapshot.params['id'];

    const operation = view ? this.fs.getConfigForViewingDocument(id) : this.fs.getConfigForEditingDocument(id);

    operation.subscribe(config => {
      // const config = {
      //   document: {
      //     fileType: 'docx',
      //     title: fileInfo.name,
      //     url: this.fs.getDownloadFileUrl(fileInfo.id),
      //   },
      //   documentType: 'word',
      //   width: '100%',
      //   height: '97%',
      //   editorConfig: {
      //     mode: view ? 'view' : 'edit',
      //     lang: 'ru',
      //     callbackUrl: `${this.fs.getCallbackUrl()}`,
      //     user: {
      //       id: '1',
      //       name: 'user'
      //     },
      //     customization: {
      //       autosave: true,
      //       forcesave: true,
      //       compactHeader: false,
      //       macros: false,
      //       plugins: false,
      //       unit: 'cm',
      //       comments: false,
      //       chat: false,
      //       feedback: false
      //     }
      //   }
      // };
      this.docEditor = new DocsAPI.DocEditor('doc', config);
    })

  }

  ngOnDestroy() {
    if (this.docEditor) {
      this.docEditor.destroyEditor();
    }
  }

}

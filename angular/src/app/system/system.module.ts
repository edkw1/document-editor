import {NgModule, Provider} from "@angular/core";
import {CommonModule} from "@angular/common";
import {RouterModule} from "@angular/router";
import { DocListPageComponent } from './doc-list-page/doc-list-page.component';
import {SystemLayoutComponent} from "./shared/components/system-layout/system-layout.component";


@NgModule({
  declarations: [
    SystemLayoutComponent,
    DocListPageComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild([
      { path: '', pathMatch: 'full', redirectTo: '/system/docs'},
      {
        path: '',
        component: SystemLayoutComponent,
        children: [
          {path: 'docs', component: DocListPageComponent}
        ]
      },
    ])
  ],
  exports: [
    RouterModule
  ]
})
export class SystemModule {
}

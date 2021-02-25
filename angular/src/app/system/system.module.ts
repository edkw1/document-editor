import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {Route, RouterModule} from "@angular/router";
import {DocListPageComponent} from './doc-list-page/doc-list-page.component';
import {SystemLayoutComponent} from "./shared/components/system-layout/system-layout.component";
import {DocEditPageComponent} from './doc-edit-page/doc-edit-page.component';
import { HeaderComponent } from './shared/components/header/header.component';


const routes: Route[] = [
  { path: '', pathMatch: 'full', redirectTo: '/system/docs'},
  {
    path: '',
    component: SystemLayoutComponent,
    children: [
      {path: 'docs', component: DocListPageComponent},
      {path: 'edit/:id', component: DocEditPageComponent},
      {path: 'view/:id', component: DocEditPageComponent, data: {view: true}},
      {path: '**', redirectTo: '/system/docs'}
    ]
  },
]



@NgModule({
  declarations: [
    SystemLayoutComponent,
    DocListPageComponent,
    DocEditPageComponent,
    HeaderComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class SystemModule {
}

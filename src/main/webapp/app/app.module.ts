import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { HkjcSharedModule } from 'app/shared/shared.module';
import { HkjcCoreModule } from 'app/core/core.module';
import { HkjcAppRoutingModule } from './app-routing.module';
import { HkjcHomeModule } from './home/home.module';
import { HkjcEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    HkjcSharedModule,
    HkjcCoreModule,
    HkjcHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    HkjcEntityModule,
    HkjcAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class HkjcAppModule {}

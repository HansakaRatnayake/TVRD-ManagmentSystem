import { Routes } from '@angular/router';
import {NotfoundpageComponent} from "./shared/notfoundpage/notfoundpage.component";
import {MainwindowComponent} from "./shared/mainwindow/mainwindow.component";
import {HomeComponent} from "./view/home/home.component";
import {DashboardComponent} from "./view/dashboard/dashboard.component";

import {authGuard} from "./core/guard/auth.guard";
import {EmployeeComponent} from "./modules/employee/employee.component";
import {UserComponent} from "./modules/user/user.component";
import {authenticationGuard} from "./core/interceptor/authentication.guard";
import {AuthComponent} from "./core/auth/auth.component";
import {MohdetailsComponent} from "./modules/mohdetails/mohdetails.component";
import {ProducorderComponent} from "./modules/producorder/producorder.component";
import {CountbymohComponent} from "./core/report/view/countbymoh/countbymoh.component";
import {CountbyporderComponent} from "./core/report/view/countbyporder/countbyporder.component";
import {VehicleComponent} from "./modules/vehicle/vehicle.component";
import {VaccineComponent} from "./modules/vaccine/vaccine.component";

export const routes: Routes = [
  { path: "login", component: AuthComponent },
  { path: "", redirectTo:"login" ,pathMatch:"full"},
  {
    path: "main",
    component: MainwindowComponent,
    canActivate:[authenticationGuard],
    children: [
      {path: "home", component: HomeComponent,title: "Home"},
      {path: "dashboard", component: DashboardComponent,title: "Dashboard"},
      {path: "employee", component: EmployeeComponent,title: "Employee"},
      {path: "user", component: UserComponent,title: "User"},
      {path: "mohdetail", component: MohdetailsComponent,title: "MOH Details"},
      {path: "productorder", component: ProducorderComponent,title: "Product Order"},
      {path: "vehicle", component: VehicleComponent, title: "Vehicle Details"},
      {path: "vaccine", component: VaccineComponent, title: "Vaccine Details"},


      {path: "mohreport", component: CountbymohComponent,title: "MOH Report"},
      {path: "porderreport", component: CountbyporderComponent,title: "POrder Report"},
      {path:"**", component:NotfoundpageComponent}

    ]
  },

];

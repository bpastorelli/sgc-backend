import { properties } from './../../properties/properties';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../_services/authentication.service';
import { ResidenciasService } from './residencias.service';
import { ErroRegistro } from '../_models/erro-registro';
import { ResidenciasFilterModel } from './residencias-filter.model';
import { ResidenciaResponse } from './residencia-response.model';
import { PerfilFuncionalidade } from '../acessos-funcionalidades/acesso-funcionalidade.model';
import { PermissoesService } from '../_services/permissoes.service';

@Component({
  selector: 'app-residencias',
  templateUrl: './residencias.component.html'
})
export class ResidenciasComponent implements OnInit {

  residencias: ResidenciaResponse[] = [];

  public ticket: string;

  pag : Number = 1 ;
  contador : Number = properties.itemsPerPage;

  erros: ErroRegistro[] = [];

  perfil = {} as PerfilFuncionalidade[];
  perfilVisita = {} as PerfilFuncionalidade;

  title = "Cadastro de Residências";

  requestDto: ResidenciasFilterModel = new ResidenciasFilterModel();

  constructor(
      private residenciasService: ResidenciasService,
      private authenticationService: AuthenticationService,
      private router: Router,
      private permissao: PermissoesService,
      private route: ActivatedRoute,
  ) { }

  ngOnInit() {

    //this.ticket = this.route.snapshot.paramMap.get('ticket');

    if(this.authenticationService.currentUserValue){
      this.getResidencias();
    }else{
      this.router.navigate(['/login']);
    }

  }

  getResidencias(codigo?: string, endereco?: string, numero?: string){

    this.requestDto = new ResidenciasFilterModel();
    this.perfil = [];

    if(codigo)
      this.requestDto.id = codigo;

    
    if(endereco)
      this.requestDto.endereco = endereco;

    if(numero)
      this.requestDto.numero = numero;

    //if(this.ticket)
    //  this.requestDto.guide = this.ticket;

    let modulos: string[] = [];
    let funcionalidades: string[] = [];

    modulos.push('3','6');
    funcionalidades.push('7','14');

    this.residenciasService.residencias(this.requestDto)
    .subscribe(
      data=>{
        this.residencias = data;
        this.permissao.getPermissao(modulos, funcionalidades)
          .subscribe(
            data=>{
              this.perfil = data;
            }, err=>{
              console.log(err['erros']);
            }
          );
      }, err=>{
        this.erros = err['erros'];
      }
    );
    return this.residencias;

  }

  getResidenciaById(codigo: string){

    this.requestDto = new ResidenciasFilterModel();

    if(codigo)
      this.requestDto.id = codigo;

    this.residenciasService.residencias(this.requestDto)
    .subscribe(
        data=>{
          this.residencias = data;
        }, err=>{
          this.erros = err['erros'];
        }
    );
    return this.residencias;

  }

  incluirMorador(codigo: string){

    this.router.navigate([`morador/create/residencia/`, codigo])

  }

  incluirVisita(codigo: string){

    this.router.navigate([`visita/residencia/`, codigo])

  }

  editResidencia(codigo: string){

    this.router.navigate([`/residencia/view/`, codigo])

  }

  viewResidencia(codigo: string){

    this.router.navigate([`/residencia/view/`, codigo])

  }

  pageChanged(event){
    this.pag = event;
  }

  formatId (n, len) {
    var num = parseInt(n, 10);
    len = parseInt(len, 10);
    return (isNaN(num) || isNaN(len)) ? n : ( 1e10 + "" + num ).slice(-len);
  }

}

import { ActivatedRoute, Router } from '@angular/router';
import { VeiculosService } from './../veiculos.service';
import { Veiculo } from './../veiculo.model';
import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { ErroRegistro } from 'src/app/_models/erro-registro';
import { PermissoesService } from 'src/app/_services/permissoes.service';
import { PerfilFuncionalidade } from 'src/app/acessos-funcionalidades/acesso-funcionalidade.model';

@Component({
  selector: 'app-veiculo',
  templateUrl: './veiculo.component.html'
})
export class VeiculoComponent implements OnInit {

  id:           number;
  acao:         string;
  codigo:       string;
  create:       boolean = true
  veiculo:      Veiculo;
  veiculos:     Veiculo[];
  ticket:       string;
  errorMessage;
  pag: Number = 1;
  contador: Number = 5;
  erros: ErroRegistro[] = [];
  perfil = {} as PerfilFuncionalidade;
  
  title = "Cadastro de Veículos";

  situacaoCadastral = [
        { id: 1, label: "ATIVO" },
        { id: 0, label: "INATIVO" }]

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private veiculosService: VeiculosService,
      private authenticationService: AuthenticationService,
      private permissao: PermissoesService
  ) { }

  ngOnInit(): void {

    this.acao = this.route.snapshot.paramMap.get('acao');
    this.codigo = this.route.snapshot.paramMap.get('codigo');
    
    if(this.authenticationService.currentUserValue){
      if(this.acao != "create" && this.acao != "novo"){
        this.create = false;
          this.getVeiculoById(this.codigo);
          this.permissao.getPermissao('5', '16')
          .subscribe(
            data=>{
              this.perfil = data[0];
            }, err=>{
              console.log(err['erros']);
            }
          );
      }else{
        this.permissao.getPermissao('5', '15')
          .subscribe(
            data=>{
              this.perfil = data[0];
            }, err=>{
              console.log(err['erros']);
            }
          );
      }
    }else{
        this.router.navigate(['/login']);
    }

  }

  postVeiculo(veiculo: Veiculo){

    if(this.codigo != "create")
      veiculo.ticketVisitante = this.codigo;

    this.veiculosService.postVeiculo(veiculo)
      .subscribe(data => {
        this.id = data.ticket;
        this.router.navigate([`/summary-add`]);
    },err=>{
        this.erros = err['erros'];
    });

  }

  editVeiculo(id: string){

    this.acao = 'edit';
    this.router.navigate(['/veiculo/edit/', id]);

  }

  postVeiculoAmqp(veiculo: Veiculo){

    if(this.codigo != "create")
      veiculo.ticketVisitante = this.codigo;
    
    this.veiculosService.postVeiculoAmqp(veiculo)
      .subscribe(data => {
        this.id = data.ticket;
        this.router.navigate([`/summary-add`]);
    },err=>{
        this.erros = err['erros'];
    });

  }

  putVeiculo(veiculo: Veiculo, id: string){

    this.veiculosService.putVeiculo(veiculo, id)
      .subscribe(data => {
        this.ticket = data.ticket;
        this.router.navigate([`/summary-edit`]);
    },err=>{
        this.erros = err['erros'];
    });

  }

  getVeiculoById(id: string){

    this.veiculosService.getVeiculoById(id)
      .subscribe(data => {
        this.veiculos = data;
    },err=>{
        this.errorMessage = err;
    });

  }

  cancelar(){

    this.router.navigate(['veiculos'])

  }

  editVisitante(codigo: string){

    this.acao = 'view';
    this.router.navigate([`/visitante/view/`, codigo])

  }

  pageChanged(event){
    this.pag = event;
  }

}

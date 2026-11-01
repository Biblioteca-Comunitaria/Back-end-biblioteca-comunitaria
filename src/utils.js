export const KEY="biblioteca-luiz-brasil-v2";
export const SESSION="biblioteca-luiz-brasil-sessao";
export const DAY=86400000;
export const getRoute=()=>{const h=location.hash.slice(1);return["livros","emprestimos","config"].includes(h)?h:"livros"};
export const iso=d=>{const x=new Date(d);return x.getFullYear()+"-"+String(x.getMonth()+1).padStart(2,"0")+"-"+String(x.getDate()).padStart(2,"0")};
export const off=n=>iso(Date.now()+n*DAY);
export const fmt=s=>{const[y,m,d]=s.split("-");return new Date(y,m-1,d).toLocaleDateString("pt-BR",{day:"numeric",month:"short",year:"numeric"})};
export const digits=s=>s.replace(/\D/g,"");
export const norm=s=>s.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g,"");

// Script de dados iniciais (leitores ficticios, livros e emprestimos de exemplo)
export function seed(){
  return{
    readers:[
      {id:1,name:"Renally Veríssimo",cpf:"111.222.333-44",phone:"(81) 90000-0001"},
      {id:2,name:"Abigail Maria",cpf:"222.333.444-55",phone:"(81) 90000-0002"},
      {id:3,name:"João Pedro Lima",cpf:"333.444.555-66",phone:"(81) 90000-0003"},
      {id:4,name:"Maria Clara Souza",cpf:"444.555.666-77",phone:"(81) 90000-0004"},
      {id:5,name:"Carlos Henrique",cpf:"555.666.777-88",phone:"(81) 90000-0005"},
      {id:6,name:"Ana Beatriz Rocha",cpf:"666.777.888-99",phone:"(81) 90000-0006"}],
    books:[
      {id:1,title:"Casa-Grande & Senzala",author:"Gilberto Freyre",genre:"Sociologia",publisher:"Global",year:1933,isbn:"978-0000-0001",copies:4,status:"Disponível"},
      {id:2,title:"Dom Casmurro",author:"Machado de Assis",genre:"Romance",publisher:"Penguin Companhia",year:1899,isbn:"978-0000-0002",copies:2,status:"Disponível"},
      {id:3,title:"Vidas Secas",author:"Graciliano Ramos",genre:"Romance",publisher:"Record",year:1938,isbn:"978-0000-0003",copies:1,status:"Disponível"},
      {id:4,title:"Capitães da Areia",author:"Jorge Amado",genre:"Romance",publisher:"Companhia das Letras",year:1937,isbn:"978-0000-0004",copies:3,status:"Disponível"},
      {id:5,title:"A Hora da Estrela",author:"Clarice Lispector",genre:"Romance",publisher:"Rocco",year:1977,isbn:"978-0000-0005",copies:2,status:"Disponível"},
      {id:6,title:"O Quinze",author:"Rachel de Queiroz",genre:"Romance",publisher:"José Olympio",year:1930,isbn:"978-0000-0006",copies:1,status:"Indisponível"}],
    loans:[
      {id:1,bookId:1,readerId:1,date:off(-20),due:off(-6),returned:null},
      {id:2,bookId:2,readerId:2,date:off(-18),due:off(-4),returned:null},
      {id:3,bookId:3,readerId:2,date:off(-3),due:off(11),returned:null},
      {id:4,bookId:4,readerId:3,date:off(-60),due:off(-46),returned:off(-50)}]};
}
export function load(){try{const r=localStorage.getItem(KEY);if(r)return JSON.parse(r)}catch(e){}return seed()}

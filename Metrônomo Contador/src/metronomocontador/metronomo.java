package metronomocontador;

import java.util.Timer;
import java.util.TimerTask;

import java.io.InputStream;
import java.io.FileInputStream;  
import java.io.File;
import javax.swing.JTextField;

import sun.audio.*;    


//classe metrônomo
public class metronomo{
    //atributos que serão enviados pelo formulário ou por outro lugar
    private int BPM;
    private int Tempos;
    private int Nota;
    
    private int QuantidadeExercicios;
    private int CompassosPorExercicio;
    private int CompassosExecutados;
    private int ExerciciosRealizados;
    
    private int exerciciosRealizados;
    private JTextField txtExerciciosRealizados;
    
    //atributos que serão usados apenas nessa classe
    private double sleep_timer;
    private int tempoatual;        
    
    //cria um novo objeto Timer da classe que foi importada importado
    Timer timer = new Timer();
    
    //Construtor que 'pede' parâmetros
    public metronomo(int BPM, int Tempos, int Nota) {
        setBPM(BPM);
	setTempos(Tempos);
	setNota(Nota);
    }
    
    public metronomo(int BPM, int Tempos, 
                     int Nota, 
                     int quantidadeExercicios, 
                     int compassosPorExercicio, 
                     JTextField txtExerciciosRealizados) {
        
        setBPM(BPM);
	setTempos(Tempos);
	setNota(Nota);
        
        setCompassosExecutados(0);
        setExerciciosRealizados(0);
        
        setQuantidadeExercicios(quantidadeExercicios);
        setCompassosPorExercicio(compassosPorExercicio);
        setTxtExerciciosRealizados(txtExerciciosRealizados);
    }
    
    //Métodos GET e SET
    public int getBPM() {
        return BPM;
    }
    
    public void setBPM(int BPM) {
        this.BPM = BPM;
    }

    public int getTempos() {
        return Tempos;
    }

    public void setTempos(int Tempos) {
        this.Tempos = Tempos;
    }

    public int getNota() {
        return Nota;
    }

    public void setNota(int Nota) {
        this.Nota = Nota;
    }
   
    public int getQuantidadeExercicios() {
        return QuantidadeExercicios;
    }

    public void setQuantidadeExercicios(int QuantidadeExercicios) {
        this.QuantidadeExercicios = QuantidadeExercicios;
    }

    public int getCompassosPorExercicio() {
        return CompassosPorExercicio;
    }

    public void setCompassosPorExercicio(int CompassosPorExercicio) {
        this.CompassosPorExercicio = CompassosPorExercicio;
    }

    public int getExerciciosRealizados() {
        return ExerciciosRealizados;
    }

    public void setExerciciosRealizados(int ExerciciosRealizados) {
        this.ExerciciosRealizados = ExerciciosRealizados;
    }
    
    /**
     * @return the txtExerciciosRealizados
     */
    public JTextField getTxtExerciciosRealizados() {
        return txtExerciciosRealizados;
    }

    /**
     * @param txtExerciciosRealizados the txtExerciciosRealizados to set
     */
    public void setTxtExerciciosRealizados(JTextField txtExerciciosRealizados) {
        this.txtExerciciosRealizados = txtExerciciosRealizados;
    }
    
    public double getSleep_timer() {
        return sleep_timer;
    }

    public void setSleep_timer(double sleep_timer) {
        this.sleep_timer = sleep_timer;
    }

    public int getTempoatual() {
        return tempoatual;
    }

    public void setTempoatual(int tempoatual) {
        this.tempoatual = tempoatual;
    }
    
    //fim dos métodos GET e SET
    
    //método principal que será usado para iniciar o metrônomo
    public void run() {
         
        //fórmula usada para transformar as Batidas por Minuto em um número compatível com o timer
        setSleep_timer( 1.0 / getBPM() * 60000.0);
        
        //iniciando o timer pela classe BEEP, que executará o som a cada intervalo de tempo definido no comando acima 
        timer.schedule(new metronomo.beep(), 0, (long) getSleep_timer());
    }     

    /**
     * @return the CompassosExecutados
     */
    public int getCompassosExecutados() {
        return CompassosExecutados;
    }

    /**
     * @param CompassosExecutados the CompassosExecutados to set
     */
    public void setCompassosExecutados(int CompassosExecutados) {
        this.CompassosExecutados = CompassosExecutados;
    }
    
    //classe do beep
    public class beep extends TimerTask {
        
        @Override
        public void run() {
                
                //se a variável tempoatual for 0, some um tempo (começa no 1º) e termine o método
                if (getTempoatual() == 0) {
                    setTempoatual(getTempoatual() + 1);                    
                    return;
                }
                
                // se for o primeiro tempo, o beep será forte, caso o contrário, fraco
                if (getTempoatual() == 1) {
                    //chama o método do beep forte(ver abaixo)
                    BeepForte();
                }
                else {
                    //chama o método do beep fraco(ver abaixo)
                    BeepFraco();
                }
                
                //se o tempo atual atingir a quantidade de tempos de compasso definida pelo usuário, é zerado o compasso
                if (tempoatual == getTempos()) {
                    
                    setCompassosExecutados(getCompassosExecutados() + 1);
                    
                    // Se quantidade de compassos for menor que quantidade de compassos do exercicio
                    if (getCompassosExecutados() == getCompassosPorExercicio())
                    {
                        setExerciciosRealizados(getExerciciosRealizados() + 1);
                        setCompassosExecutados(0);
                    }
                                        
                    getTxtExerciciosRealizados().setText(""+getExerciciosRealizados());
                    
                    if (getExerciciosRealizados() == getQuantidadeExercicios())
                    {
                        setCompassosExecutados(0);
                        setExerciciosRealizados(0);
                        timer.cancel();
                    }
                    
                    
                    
                    // zerando o tempo
                    setTempoatual(0);
                }
                
                // avancar um tempo
		setTempoatual(getTempoatual() + 1);
 
        }
    }

    //método para terminar a execução do programa, simplismente cancela o timer declarado no início e ligado no método run()
    public void encerrar() {
        timer.cancel();
    }
 
    
    //método do beep forte
    public void BeepForte() {
        
        try{
            //comandos necessários para tocar o som presente no arquivo 'beepforte.wav'
            //obs: acho que este arquivo fica fora do pacote .jar, o que está dentro não consegui executar ainda.
            File diretorio = new File("beepforte.wav");
            InputStream in = new FileInputStream(diretorio);
            AudioStream as = new AudioStream(in);
            AudioPlayer.player.start(as);      
       }
            
       catch(java.io.IOException e) { System.out.println(e); }    
    }
    
    public void BeepFraco() {
        
        try{
            //comandos necessários para tocar o som presente no arquivo 'beepfraco.wav'
            //obs: acho que este arquivo fica fora do pacote .jar, o que está dentro não consegui executar ainda.
            File diretorio = new File("beepfraco.wav");
            
            InputStream in = new FileInputStream(diretorio);
            AudioStream as = new AudioStream(in);     
            AudioPlayer.player.start(as);      
        }
            
       catch(java.io.IOException e) { System.out.println(e); }    
    }
    
}
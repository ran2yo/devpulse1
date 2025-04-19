public class SampleApp {
    public static void main(String[] args) {
        new SampleApp();
    }


    public void run(){
        System.out.println("작업 수행 중");
        try {
            Thread.sleep(300);
        }catch (InterruptedException e){}
    }
}

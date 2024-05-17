package GameMain;

import java.util.Scanner;

import GAMESTAGE.GameStage;




public class GameMain 
{
    private GameStage stage;
    

    private final int maxMapNum = 4;
    
    private final String fileName = "Data.ser";

    private static Scanner input = new Scanner(System.in);

//----------------------------------------------------------------

    //Constructor
    public GameMain()
    {

    }


    
    public void Run()
    {
        int choice;
        do
        {      
            System.out.println("\n******************************************************************");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> HOME MENU <<<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println("******************************************************************");
            System.out.println("1. New Game");
            System.out.println("2. Continue Game");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
            input.nextLine();                //Consume keyboard buffer

            switch (choice) 
            {
                case 1:
                        this.stage = new GameStage();
                        startStage();
                        break;

                case 2: // reminder: add the sistuation where it cannot find the file
                        this.stage = GameStage.load(fileName);
                        if(this.stage == null)
                        {
                            System.out.println("Not Found Current Game Data!");
                            break;
                        }
                        this.stage.setExitState(false);
                        startStage();
                        break;

                case 3:

                        System.out.println("See you next time!");
                        input.close();
                        break;

                default:
                        System.out.println("ERROR: Invalid Choice!");
                        break;

            }
        } while (choice != 3);
    }
 
    //run a specific stage with current player, map, inventory (Notice when player want to pause program)

    public void startStage()  //~~ Stages Loop
    {   
        System.out.println("\n******************************************************************");
        System.out.print(">>>>>>>>>>>>>>>>>>>>>>>> Stage " + this.stage.getStageNo());
        if(this.stage.getBossPhase()>0)
            System.out.print("(Boss phase " + this.stage.getBossPhase()+")<<<<<<<<<<<<<<<<<<<<\n");
        else
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("******************************************************************");
        System.out.println("Press any key to continue.");
        input.nextLine();
        stage.showGraphic();
        do 
        {
            stage.playerAction();
            if(!stage.isExit())
            {
                stage.monsterAction();
                stage.updateMap();
                stage.showGraphic();
            }
            stage.save(fileName);
        } while (!stage.win() && !stage.loose() && !stage.isExit());     //==> status of stage = 

         
        if(stage.loose())
        {
            stage.resetstage();
            stage.resetPlayerWhenDied();
            stage.delete(fileName);
            System.out.println(">> You died!! Let's start at beginning (press any key to continue):" );
            input.nextLine();
        }
                 
        else if(stage.win())
        {
            if(!(this.stage.getBossPhase()>0))
            {   
                while(!this.stage.isPlayeratDoor() && !stage.isExit())
                {
                    stage.playerAction();
                    stage.updateMap();
                    stage.showGraphic();
                    stage.save(fileName);
                }

                if(this.stage.isPlayeratDoor())
                {
                    System.out.println("You enter the door and cleared stage "+ this.stage.getStageNo() + "! (press any key to coutinue.)");
                    input.nextLine();
                    this.stage.resetPlayerWhenNextStage();
                    this.stage.nextmap();
                    this.startStage();
                    stage.save(fileName);
                }
                
            }
            else
            {
                if(this.stage.getCurrentMapNo() != this.maxMapNum)
                {   
                    System.out.println("You have defeated the boss. However you feel chill down your spine.(press any key to continue)");
                    input.nextLine();
                    System.out.println("The boss has awoken, prepare to fight!(press any key to continue)");
                    input.nextLine();
                    this.stage.nextmap();
                    this.startStage();
                    stage.save(fileName);
                }
                else
                {
                    while(!this.stage.isPlayeratDoor() && !stage.isExit())
                    {
                        stage.playerAction();
                        stage.updateMap();
                        stage.showGraphic();
                        stage.save(fileName);
                    }

                    if(this.stage.isPlayeratDoor())
                    {
                        int choice =0;
                        System.out.println("CONGRATULATION! YOU WIN ENTIRE GAME!!! (Press any key to continue):");
                        input.nextLine();
                        stage.save(fileName);
                        System.out.println("Do you want to carry on the items you achived and go on another jorney? (0:No | 1:Yes)");
                        choice = input.nextInt();
                        switch (choice) {
                            case 0:
                                System.out.println("You will now be sent back to Menu (press any key to continue)");
                                input.nextLine();
                                break;
                            case 1:
                                System.out.println("You will now go on another journey!(press any key to continue)");
                                input.nextLine();
                                this.stage.resetstage();
                                this.stage.resetPlayerWhenNextStage();
                                this.startStage();
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            
        }     
    }


    public static void main(String[] args) {
        GameMain gmt = new GameMain();
        gmt.Run();
    }
}

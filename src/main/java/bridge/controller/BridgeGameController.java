package bridge.controller;

import bridge.BridgeRandomNumberGenerator;
import bridge.model.*;
import bridge.view.InputView;
import bridge.view.OutputView;

public class BridgeGameController {
    private final OutputView outputView;
    private final InputView inputView;

    private BridgeGame bridgeGame;

    public BridgeGameController() {
        outputView = new OutputView();
        inputView = new InputView();
    }

    public void runGame() {
        try {
            runGameWithoutExceptionControl();
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
        }
    }

    private void runGameWithoutExceptionControl() {
        printGameStartAlert();

        int bridgeSize = getAndProcessBridgeLengthInput();
        makeRandomBridge(bridgeSize);

        boolean willRetry = true;
        do {
            BridgeLane nextMove = getAndProcessNextMovementInput();
            bridgeGame.move(nextMove);
            printBridgeMap();

            if(bridgeGame.getResult().isSuccess()) {
                willRetry = false;
                continue;
            }

            if(bridgeGame.getResult().isFail()) {
                RestartInfo gameCommand = getAndProcessGameCommandInput();
                if(gameCommand == RestartInfo.RETRY) {
                    bridgeGame.retry();
                    continue;
                }
                if(gameCommand == RestartInfo.QUIT) {
                    willRetry = false;
                }
            }
        } while(willRetry);

        outputView.printResult(bridgeGame.getBridge(), bridgeGame.getResult());
    }

    private RestartInfo getAndProcessGameCommandInput() {
        outputView.printRetryInputAlert();
        return inputView.readGameCommand();
    }

    private void printBridgeMap() {
        outputView.printMap(bridgeGame);
    }

    private BridgeLane getAndProcessNextMovementInput() {
        outputView.printNextMovementInputAlert();
        return inputView.readMoving();
    }

    private void printGameStartAlert() {
        outputView.printGameStartAlert();
    }

    private int getAndProcessBridgeLengthInput() {
        outputView.printBridgeSizeInputAlert();
        return inputView.readBridgeSize();
    }

    private void makeRandomBridge(int bridgeSize) {
        BridgeMaker bridgeMaker = new BridgeMaker(new BridgeRandomNumberGenerator());
        bridgeGame = new BridgeGame(new Bridge(bridgeMaker.makeBridge(bridgeSize)));
    }
}

package bridge.model;

/**
 * 다리 건너기 게임을 관리하는 클래스
 */
public class BridgeGame {
    private final Bridge bridge;
    private final BridgeGameResult result;
    private int currentPosition;

    public BridgeGame(Bridge bridge) {
        this.bridge = new Bridge(bridge);
        result = new BridgeGameResult();
        currentPosition = 0;
    }

    /**
     * 사용자가 칸을 이동할 때 사용하는 메서드
     * <p>
     * 이동을 위해 필요한 메서드의 반환 타입(return type), 인자(parameter)는 자유롭게 추가하거나 변경할 수 있다.
     */
    public boolean move(BridgeLane spotInfo) {
        boolean isOnSafeSpot = bridge.isSafeSpot(++currentPosition, spotInfo);
        result.addMovementOnLastMovementRecord(spotInfo);

        if(!isOnSafeSpot) {
            result.makeResultFail();
        }

        if(isOnSafeSpot && currentPosition == bridge.getBridgeSize()) {
            result.makeResultSuccess();
        }
        return isOnSafeSpot;
    }

    /**
     * 사용자가 게임을 다시 시도할 때 사용하는 메서드
     * <p>
     * 재시작을 위해 필요한 메서드의 반환 타입(return type), 인자(parameter)는 자유롭게 추가하거나 변경할 수 있다.
     */
    public void retry() {
        currentPosition = 0;
        result.resetFlag();
        result.addTryCount();
        result.clearLastMovementRecord();
    }

    public BridgeGameResult getResult() {
        return new BridgeGameResult(result);
    }

    public Bridge getBridge() {
        return new Bridge(bridge);
    }
}

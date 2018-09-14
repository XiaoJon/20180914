package com.hjhq.teamface.componentservice.project;

/**
 * 工作台
 *
 * @author Administrator
 * @date 2018/6/1
 */

public interface WorkBenchService {
    void setTextIndex(int index);

    void canDragItemAtPosition(int column, int dragPosition);

    void canDropItemAtPosition(int oldColumn, int oldRow, int newColumn, int newRow);
}

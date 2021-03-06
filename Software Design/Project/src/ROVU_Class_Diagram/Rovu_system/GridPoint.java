// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package ROVU_Class_Diagram.Rovu_system;

import ROVU_Class_Diagram.Rovu_system.GridPointStatus;
import javax.vecmath.Vector3d;

/************************************************************/
/**
 * 
 */
public class GridPoint {

	public GridPointStatus gridpointstatus;
	private Vector3d coordinates;
	public static final double RANGE = 0.5;

	public GridPoint(Vector3d coord, GridPointStatus status) {
		this.coordinates = coord;
		this.gridpointstatus = status;
	}
	
	public Vector3d getCoordinates() {
		return coordinates;
	}
	
	public void setStatus(GridPointStatus status) {
		this.gridpointstatus = status;
	}

	public GridPointStatus getStatus() {
		return this.gridpointstatus;
	}
};

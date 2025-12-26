import pytest
import json
from plib.base import Point


@pytest.fixture
def points():
    return Point(0, 0), Point(2, 2)


class TestPoint:

    def test_creation(self):
        p = Point(1, 2)
        assert p.x == 1 and p.y == 2

        with pytest.raises(TypeError, match="should be an integer type"):
            Point(1.5, 2)

        with pytest.raises(TypeError, match="should be an integer type"):
            Point(1, "2")

    def test_add(self, points):
        p1, p2 = points
        res = p2 + p1
        assert res == Point(2, 2)
        assert p1.x == 0
        assert p2.x == 2

    def test_iadd(self):
        p1 = Point(1, 1)
        p2 = Point(2, 2)
        original_id = id(p1)

        p1 += p2

        assert p1.x == 3
        assert p1.y == 3
        assert p1 is not p2

    def test_sub(self, points):
        p1, p2 = points
        assert p2 - p1 == Point(2, 2)
        assert p1 - p2 == Point(-2, -2)

    def test_neg(self):
        p = Point(3, -4)
        neg_p = -p
        assert neg_p.x == -3
        assert neg_p.y == 4

    def test_equality(self):
        p1 = Point(1, 1)
        p2 = Point(1, 1)
        p3 = Point(1, 2)

        assert p1 == p2
        assert p1 != p3

        with pytest.raises(NotImplementedError):
            p1 == "String"

    def test_distance_to(self):
        p1 = Point(0, 0)
        p2 = Point(3, 4)
        assert p1.to(p2) == 5.0

    @pytest.mark.parametrize(
        "p1, p2, distance",
        [(Point(0, 0), Point(0, 10), 10),
         (Point(0, 0), Point(10, 0), 10),
         (Point(0, 0), Point(1, 1), 1.414)]
    )
    def test_distance_all_axis(self, p1, p2, distance):
        assert p1.to(p2) == pytest.approx(distance, 0.001)

    def test_str_and_repr(self):
        p = Point(5, 10)
        expected = "Point(5, 10)"

        assert str(p) == expected
        assert repr(p) == expected

    def test_is_center(self):
        assert Point(0, 0).is_center() is True

        assert Point(1, 0).is_center() is False
        assert Point(0, 1).is_center() is False
        assert Point(1, 1).is_center() is False

    def test_json_serialization(self):
        p = Point(10, 20)

        json_str = p.to_json()
        assert isinstance(json_str, str)
        assert '"x": 10' in json_str
        assert '"y": 20' in json_str

        p_new = Point.from_json(json_str)
        assert isinstance(p_new, Point)
        assert p_new == p
